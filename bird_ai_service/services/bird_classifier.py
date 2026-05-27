import librosa
import torch
import torch.nn.functional as F  # Thêm dòng này
from transformers import ASTForAudioClassification, ASTFeatureExtractor

class BirdClassifier:
    def __init__(self, model_path="./models"):
        self.feature_extractor = ASTFeatureExtractor.from_pretrained(model_path)
        self.model = ASTForAudioClassification.from_pretrained(model_path)
        self.model.eval()

    def predict(self, audio_file_path):
        y, sr = librosa.load(audio_file_path, sr=16000)
        inputs = self.feature_extractor(y, sampling_rate=sr, return_tensors="pt")

        with torch.no_grad():
            outputs = self.model(**inputs)

        logits = outputs.logits
        
        # 1. Dùng hàm Softmax để chuyển đổi logits thành xác suất (từ 0 đến 1)
        probabilities = F.softmax(logits, dim=-1)

        # 2. Lấy ra Top 5 class có tỷ lệ cao nhất
        top5_probs, top5_indices = torch.topk(probabilities, 5)

        results = []
        # 3. Lặp qua 5 kết quả này để format lại thành mảng JSON
        for i in range(5):
            prob_val = top5_probs[0][i].item() * 100  # Nhân 100 để ra phần trăm
            class_id = top5_indices[0][i].item()
            label = self.model.config.id2label[class_id]
            
            results.append({
                "bird_code": label,
                "confidence": round(prob_val, 2) # Làm tròn 2 chữ số thập phân (VD: 95.45)
            })
            
        return results