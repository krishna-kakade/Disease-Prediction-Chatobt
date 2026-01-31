import json

def format_response(raw_output: str) -> dict:
    """
    Try to parse raw_output as JSON; if fails wrap it in text field.
    """
    # if LLM returned JSON, return parsed
    try:
        parsed = json.loads(raw_output)
        # If parsed is dict and looks like conditions/advice, return directly
        if isinstance(parsed, dict):
            return parsed
        else:
            return {"text": parsed}
    except Exception:
        # not json: return as free text
        return {"text": raw_output}
