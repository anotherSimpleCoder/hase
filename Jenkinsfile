pipeline {
    agent any

    stages {
        stage('Set RSA Keys') {
            steps {
                withCredentials([
                    file(credentialsId: 'hase-backend-private-key', variable: 'PRIVATE_KEY'),
                    file(credentialsId: 'hase-backend-public-key', variable: 'PUBLIC_KEY')
                ]) {
                    sh 'mkdir backend/src/main/resources/certs'
                    dir('backend/src/main/resources/certs') {
                            sh 'cp $PRIVATE_KEY ./private.pem'
                            sh 'cp $PUBLIC_KEY ./public.pem'
                    }
                }
            }
        }
    }
}