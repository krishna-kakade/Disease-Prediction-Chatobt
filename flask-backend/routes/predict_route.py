from flask import Blueprint, request, jsonify
from services.prompt_builder import build_prompt
from services.ollama_service import query_ollama

predict_bp = Blueprint("predict", __name__)

@predict_bp.route("/predict", methods=["POST"])
def predict():
    """
    Receives symptoms from the client, sends a prompt to Ollama CLI,
    and returns the model's response in a clean JSON format.
    """
    data = request.get_json(force=True)
    symptoms = data.get("symptoms", "").strip()

    if not symptoms:
        return jsonify({"status": "error", "message": "No symptoms provided"}), 400

    prompt = build_prompt(symptoms)

    try:
        # Query Ollama CLI
        model_output = query_ollama(prompt)

        # Return clean response to GUI
        return jsonify({
            "status": "success",
            "message": model_output
        })

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500
