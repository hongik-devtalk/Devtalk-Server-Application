FROM nginx:latest

# 기본 (가상공간) 설정 삭제
RUN rm /etc/nginx/conf.d/nginx.conf
# 내 설정 복사
COPY nginx.conf /etc/nginx/conf.d