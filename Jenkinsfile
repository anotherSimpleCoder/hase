pipeline {
    agent any

    stages {
        stage('Set RSA Keys') {
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
    }
}