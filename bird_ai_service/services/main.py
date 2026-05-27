from fastapi import FastAPI, UploadFile, File
import shutil
import os
from .bird_classifier import BirdClassifier

app = FastAPI(title="Bird Sound AI Service")

# Load sẵn model lên RAM khi vừa bật server
classifier = BirdClassifier(model_path="./models")

@app.post("/predict")
async def predict_bird_sound(audio_file: UploadFile = File(...)):
    # Lưu file audio nhận được ra nháp
    temp_file_path = f"temp_{audio_file.filename}"
    
    with open(temp_file_path, "wb") as buffer:
        shutil.copyfileobj(audio_file.file, buffer)
        
    try:
        # Gọi model ra nhận diện
        bird_name = classifier.predict(temp_file_path)
        
        # Xóa file nháp đi cho nhẹ ổ cứng
        os.remove(temp_file_path)
        
        return {
            "status": "success",
            "filename": audio_file.filename,
            "predicted_bird": bird_name
        }
        
    except Exception as e:
        if os.path.exists(temp_file_path):
            os.remove(temp_file_path)
        return {"status": "error", "message": str(e)}