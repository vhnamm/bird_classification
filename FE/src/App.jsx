
import React, { useState, useRef, useCallback } from 'react'
import axios from 'axios'
import './App.css'

const API = 'http://localhost:8080/api/predictions'
const ACCEPT = ['.mp3', '.wav', '.ogg']

/* ── Waveform bars (static decorative) ── */
function Waveform({ active }) {
  const bars = Array.from({ length: 48 }, (_, i) => {
    const h = 15 + Math.sin(i * 0.7) * 10 + Math.sin(i * 1.3) * 8 + Math.random() * 12
    return Math.max(4, Math.min(44, h))
  })
  return (
    <div className={`waveform ${active ? 'waveform--active' : ''}`}>
      {bars.map((h, i) => (
        <div key={i} className="waveform__bar" style={{ height: h, animationDelay: `${i * 0.02}s` }} />
      ))}
    </div>
  )
}

/* ── Single confidence row ── */
function ConfBar({ item, rank, delay }) {
  const pct = Math.round(item.confidence ?? 0)
  const isTop = rank === 1
  return (
    <div className={`cbar ${isTop ? 'cbar--top' : ''}`} style={{ animationDelay: `${delay}s` }}>
      <span className="cbar__rank">#{rank}</span>
      <div className="cbar__main">
        <div className="cbar__labels">
          <span className="cbar__name">{item.vietnameseName || item.birdCode}</span>
          <span className="cbar__code">{item.birdCode}</span>
        </div>
        <div className="cbar__track">
          <div className="cbar__fill" style={{ '--w': `${pct}%`, animationDelay: `${delay + 0.1}s` }} />
        </div>
      </div>
      <span className="cbar__pct">{pct}%</span>
    </div>
  )
}

/* ── Bird detail card ── */
function BirdCard({ bird }) {
  const pct = Math.round(bird.confidenceRate ?? 0)
  return (
    <div className="bcard">
      <div className="bcard__img-wrap">
        {bird.imgURL
          ? <img src={bird.imgURL} alt={bird.vietnameseName} className="bcard__img" />
          : <div className="bcard__no-img">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.2" width="52" height="52">
                <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/>
              </svg>
            </div>
        }
        <div className="bcard__badge">{pct}%</div>
      </div>
      <div className="bcard__body">
        <p className="bcard__code">{bird.birdCode}</p>
        <h2 className="bcard__vname">{bird.vietnameseName || bird.birdCode}</h2>
        <p className="bcard__sci">{bird.sciencetificName}</p>
        {bird.description && <p className="bcard__desc">{bird.description}</p>}
      </div>
    </div>
  )
}

/* ── Main App ── */
export default function App() {
  const [file, setFile]       = useState(null)
  const [drag, setDrag]       = useState(false)
  const [loading, setLoading] = useState(false)
  const [result, setResult]   = useState(null)
  const [error, setError]     = useState(null)
  const [audioUrl, setAudioUrl] = useState(null)
  const inputRef = useRef()
  const audioRef = useRef()

  const pickFile = useCallback((f) => {
    if (!f) return
    const ext = '.' + f.name.split('.').pop().toLowerCase()
    if (!ACCEPT.includes(ext)) { setError('Chỉ chấp nhận .mp3 · .wav · .ogg'); return }
    if (f.size > 50 * 1024 * 1024) { setError('File tối đa 50MB'); return }
    setFile(f)
    setError(null)
    setResult(null)
    if (audioUrl) URL.revokeObjectURL(audioUrl)
    setAudioUrl(URL.createObjectURL(f))
  }, [audioUrl])

  const onDrop = (e) => { e.preventDefault(); setDrag(false); pickFile(e.dataTransfer.files[0]) }

  const submit = async () => {
    if (!file || loading) return
    setLoading(true); setError(null)
    try {
      const form = new FormData()
      form.append('file', file)
      const res = await axios.post(API, form)
      setResult(res.data)
    } catch (e) {
      setError(e.response?.data?.message || `Lỗi kết nối server (${e.message})`)
    } finally {
      setLoading(false)
    }
  }

  const reset = () => {
    setFile(null); setResult(null); setError(null)
    if (audioUrl) URL.revokeObjectURL(audioUrl)
    setAudioUrl(null)
  }

  return (
    <div className="app">
      {/* BG decoration */}
      <div className="bg-blob bg-blob--1" /><div className="bg-blob bg-blob--2" />

      <header className="hdr">
        <div className="hdr__brand">
          <span className="hdr__dot" />
          <span className="hdr__title">BIRD<em>ID</em></span>
        </div>
        <p className="hdr__sub">Nhận dạng loài chim qua âm thanh</p>
      </header>

      <main className="main">
        {/* ── UPLOAD SECTION ── */}
        <section className="upload-col">
          <div
            className={`drop ${drag ? 'drop--over' : ''} ${file ? 'drop--filled' : ''}`}
            onDragOver={e => { e.preventDefault(); setDrag(true) }}
            onDragLeave={() => setDrag(false)}
            onDrop={onDrop}
            onClick={() => !file && inputRef.current.click()}
          >
            <input ref={inputRef} type="file" accept=".mp3,.wav,.ogg" hidden
              onChange={e => pickFile(e.target.files[0])} />

            {!file ? (
              <div className="drop__empty">
                <div className="drop__icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" width="42" height="42">
                    <path d="M9 19V6l12-3v13"/><circle cx="6" cy="19" r="3"/><circle cx="18" cy="16" r="3"/>
                  </svg>
                </div>
                <p className="drop__cta">Kéo thả hoặc <span>chọn file</span></p>
                <p className="drop__hint">MP3 · WAV · OGG &nbsp;·&nbsp; Tối đa 50MB</p>
              </div>
            ) : (
              <div className="drop__preview">
                <Waveform active={loading} />
                <div className="drop__meta">
                  <span className="drop__fname">{file.name}</span>
                  <span className="drop__fsize">{(file.size / 1024).toFixed(0)} KB</span>
                </div>
                <button className="drop__rm" onClick={e => { e.stopPropagation(); reset() }} title="Xóa">✕</button>
              </div>
            )}
          </div>

          {/* Native audio player */}
          {audioUrl && (
            <audio ref={audioRef} controls src={audioUrl} className="audio-player" />
          )}

          {error && <p className="err">{error}</p>}

          <button
            className={`btn-run ${loading ? 'btn-run--busy' : ''}`}
            disabled={!file || loading}
            onClick={submit}
          >
            {loading
              ? <><span className="spin" />Đang phân tích...</>
              : <><span className="btn-run__dot" />Nhận dạng</>}
          </button>

          {result && (
            <button className="btn-reset" onClick={reset}>← Thử file khác</button>
          )}
        </section>

        {/* ── RESULT SECTION ── */}
        {result && (
          <section className="result-col">
            {/* Top 1 detail */}
            <div className="panel">
              <h3 className="panel__hd">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" width="14" height="14"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                Phát hiện
              </h3>
              <BirdCard bird={result.topBirdDetail} />
            </div>

            {/* Top 5 bars */}
            <div className="panel">
              <h3 className="panel__hd">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" width="14" height="14"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>
                Xác suất top 5
              </h3>
              <div className="bars">
                {result.top5.map((item, i) => (
                  <ConfBar key={item.birdCode} item={item} rank={i + 1} delay={i * 0.07} />
                ))}
              </div>
            </div>
          </section>
        )}
      </main>
    </div>
  )
}
