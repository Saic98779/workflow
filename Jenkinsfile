pipeline {
    agent any

    environment {
        IMAGE       = "workflow:${BUILD_NUMBER}"
        DEPLOY_HOST = '51.222.221.25'
        DEPLOY_USER = 'ubuntu'
        // Jenkins credential (type "SSH Username with private key") for ubuntu@51.222.221.25
        SSH_CRED_ID = 'workflow-server-ssh'
    }

    options {
        disableConcurrentBuilds()
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker image') {
            steps {
                sh 'DOCKER_BUILDKIT=1 docker build -t $IMAGE -t workflow:latest .'
            }
        }

        stage('Deploy') {
            steps {
                sshagent(credentials: [env.SSH_CRED_ID]) {
                    sh '''
                        SSH="ssh -o StrictHostKeyChecking=accept-new $DEPLOY_USER@$DEPLOY_HOST"

                        # Ship the image to the server (no registry needed)
                        docker save $IMAGE | gzip | $SSH 'gunzip | docker load'
                        $SSH "docker tag $IMAGE workflow:latest"

                        # Run the deploy script remotely
                        $SSH 'bash -s' -- "$IMAGE" < deploy/deploy.sh
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Deployed ${IMAGE} to http://${DEPLOY_HOST}:8082/workflow"
        }
    }
}
