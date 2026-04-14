pipeline {
    agent any  

    tools {
        maven 'Maven' 
    }
    
    environment {
        COMPOSE_PATH = "${WORKSPACE}/docker" // 🔁 Adjust if compose file is elsewhere
        SELENIUM_GRID = "true"
    }

    stages {
		stage('Start Selenium Grid via Docker Compose') {
            steps {
                script {
                    echo "Starting Selenium Grid with Docker Compose..."
                    bat "docker compose -f %WORKSPACE%/docker/docker-compose.yml up -d"
                    echo "Waiting for Selenium Grid to be ready..."
                    sleep 30 // Add a wait if needed
                }
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/rameshk2024/Selenium-Java-Hybrid-Framework.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install -DseleniumGrid=true'
            }
        }

        stage('Test') {
            steps {
                bat "mvn clean test -DseleniumGrid=true"
            }
        }
        
        stage('Stop Selenium Grid') {
            steps {
                script {
                    echo "Stopping Selenium Grid..."
                    bat "docker compose -f ${COMPOSE_PATH}\\docker-compose.yml down"
                }
            }
        }

        stage('Reports') {
            steps {
                publishHTML(target: [
                    reportDir: 'src/test/resources/ExtentReport',  
                    reportFiles: 'ExtentReport.html',  
                    reportName: 'Selenium Java Hybrid Automation Test Report'
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }
        
    }
}