# FROM node:16.6.1-alpine as build
# WORKDIR /app
# COPY . /app/
# RUN npm install --silent
# RUN npm install react-scripts@3.0.1 -g --silent




FROM nginx:stable-alpine
COPY build /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx/nginx.conf /etc/nginx/conf.d
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]