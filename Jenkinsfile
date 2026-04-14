pipeline {
    agent any  

    tools {
        maven 'maven-3.9.9' 
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/rameshk2024/Selenium-Java-Hybrid-Framework.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat "mvn test"
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