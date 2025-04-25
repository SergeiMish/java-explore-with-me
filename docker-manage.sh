#!/bin/bash

case "$1" in
  start)
    echo "Stopping and removing old containers..."
    docker-compose down -v
    echo "Building and starting containers..."
    docker-compose up --build -d
    ;;
  stop)
    echo "Stopping containers..."
    docker-compose down
    ;;
  restart)
    echo "Restarting containers..."
    docker-compose restart
    ;;
  logs)
    docker-compose logs -f
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|logs}"
    exit 1
    ;;
esac