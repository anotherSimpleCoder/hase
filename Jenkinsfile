stages {
        stage('Prepare Backend') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21-noble'
                    args '-u root'
                }
            }
            steps {
                withCredentials([
                    file(credentialsId: 'hase-backend-private-key', variable: 'PRIVATE_KEY'),
                    file(credentialsId: 'hase-backend-public-key', variable: 'PUBLIC_KEY')
                ]) {
                    dir('backend') {
                        sh 'mkdir -p src/main/resources/certs'
                        sh 'cp "$PRIVATE_KEY" src/main/resources/certs/private.pem'
                        sh 'cp "$PUBLIC_KEY" src/main/resources/certs/public.pem'
                        sh 'mvn clean install -DskipTests'  // Build without running tests yet
                    }
                }
                // Archive the built backend for the parallel stage
                stash includes: 'backend/**', name: 'backend-built'
            }
        }
        
        stage('Build & Test') {
            parallel {
                stage('Backend Tests & Run') {
                    agent {
                        docker {
                            image 'maven:3.9.9-eclipse-temurin-21-noble'
                            args '-u root -p 8080:8080'  // Expose backend port
                        }
                    }
                    steps {
                        unstash 'backend-built'
                        dir('backend') {
                            sh 'nohup mvn spring-boot:run > backend.log 2>&1 &'  // Start backend
                            sh 'sleep 30'  // Wait for backend to start
                            sh 'curl -f http://localhost:8080/actuator/health || (cat backend.log && exit 1)'  // Health check
                        }
                    }
                    post {
                        always {
                            dir('backend') {
                                sh 'cat backend.log || true'  // Show backend logs
                                sh 'pkill -f "spring-boot:run" || true'  // Clean up
                            }
                        }
                    }
                }
                
                stage('Frontend Test') {
                    agent {
                        docker {
                            image 'node:23.10.0-alpine3.21'
                            args '-u root:root'
                        }
                    }
                    steps {
                        dir('frontend') {
                            sh 'npm install'
                            sh 'npm run test:e2e'
                        }
                    }
                }
            }
        }
}