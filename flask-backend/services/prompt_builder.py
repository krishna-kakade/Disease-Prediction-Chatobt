def build_prompt(symptoms: str) -> str:
    """
    Builds a structured prompt for Ollama based on user symptoms.
    """
    return f"Given the following symptoms: {symptoms}, provide a likely disease diagnosis in simple language."
