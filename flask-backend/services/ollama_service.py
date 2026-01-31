import subprocess

MODEL_NAME = "mistral"  # Change to your Ollama model if needed

def query_ollama(prompt: str) -> str:
    """
    Calls Ollama CLI to generate text from the local model.

    Returns:
        str: The generated text from Ollama.
    """
    try:
        # Run Ollama CLI command
        result = subprocess.run(
            ["ollama", "run", MODEL_NAME, prompt],
            capture_output=True,
            text=True,
            check=True
        )
        return result.stdout.strip()

    except subprocess.CalledProcessError as e:
        raise RuntimeError(f"Ollama CLI call failed: {e.stderr}")
