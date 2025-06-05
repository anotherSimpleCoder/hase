pipeline {
    agent none

    stages {
        stage('Set RSA Keys') {
            agent any

            steps {
                withCredentials([
                    file(credentialsId: 'hase-backend-private-key', variable: 'PRIVATE_KEY'),
                    file(credentialsId: 'hase-backend-public-key', variable: 'PUBLIC_KEY')
                ]) {
                    dir('backend') {
                        sh 'mkdir -p src/main/resources/certs'
                        sh 'cp "$PRIVATE_KEY" src/main/resources/certs/private.pem'
                        sh 'cp "$PUBLIC_KEY" src/main/resources/certs/public.pem'
                    }
                }
            }
        }

        stage('Build backend') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-21-noble'
                    args '-u root'
                }
            }

            steps {
                dir('backend') {
                    sh 'mvn clean install'
                }
            }
        }
    }
}