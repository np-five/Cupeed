#!/bin/bash

# ===========================
# 서버 포트 점검 및 종료 스크립트 (Bash 3.x 호환)
# ===========================

# 서버명과 포트를 "서버명:포트" 형태로 배열에 저장
servers=(
  "AI_Server:20010"
  "Company_Server:20020"
  "Delivery_Server:20030"
  "Hub_Server:20040"
  "Hub_Route_Server:20050"
  "Order_Server:20060"
  "Product_Server:20070"
  "User_Server:20080"
  "Slack_Server:20090"
  "Eureka_Server:8761"
  "Config_Server:8888"
  "Gateway:8080"
)

echo "====== 서버 포트 점검 및 종료 ======"

for entry in "${servers[@]}"; do
  name=$(echo $entry | cut -d':' -f1)
  port=$(echo $entry | cut -d':' -f2)

  pid=$(lsof -ti tcp:$port)

  if [ -n "$pid" ]; then
    echo "$name (포트 $port) 실행중 PID=$pid -> 종료"
    kill -9 $pid
  else
    echo "$name (포트 $port) 실행중 아님"
  fi
done

echo "====== 완료 ======"

