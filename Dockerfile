# Step 1: Build the React application
FROM node:16-alpine AS build

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install --force

# Copy the rest of the application code
COPY . .

EXPOSE 3001
# Build the application
# RUN npm run build 

CMD ["npm","start"]
# Step 2: Serve the built application with Nginx
#FROM nginx:1.17.1-alpine

# Copy the build output to Nginx's HTML directory
#COPY --from=build /app/build /usr/share/nginx/html

# Copy custom Nginx configuration (if any)
#COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
#EXPOSE 80

# Start Nginx
#ENTRYPOINT ["nginx", "-g", "daemon off;"]
