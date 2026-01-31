from flask import Flask
from routes.predict_route import predict_bp

app = Flask(__name__)

# Register blueprint
app.register_blueprint(predict_bp, url_prefix="/api")

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=True)
