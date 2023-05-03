FROM python:3.9-alpine

WORKDIR /app

COPY . .

WORKDIR /app/backend_toy

RUN pip install --upgrade pip

RUN pip install -r requirements.txt

RUN chmod +x entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "/app/backend_toy/entrypoint.sh & tail -f /dev/null"]