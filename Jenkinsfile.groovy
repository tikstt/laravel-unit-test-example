pipeline {
    agent any

    stages {

        stage ('Compile Stage') {
            steps {
              sh "cp .env.example .env"
              sh "composer install --prefer-dist"
            }
        }

        stage('SonarQube analysis') {
          steps {
            // requires SonarQube Scanner 2.8+
            //def scannerHome = tool 'SonarQube Scanner 2.8';
            withSonarQubeEnv('jenkins-sonarqube') {
              sh "${scannerHome}/bin/sonar-scanner"
            }
          }
        }

/*
        stage ('Sonar Stage') {
            steps {
              withSonarQubeEnv('Sonar'){
                sh "sonar-scanner"
              }
            }
        }
*/
        stage ('Testing Stage') {
            steps {
              // Run any testing suites
              sh "./vendor/bin/phpunit"
            }
        }

        stage ('Deployment Stage') {
            steps {
              // If we had ansible installed on the server, setup to run an ansible playbook
              // sh "ansible-playbook -i ./ansible/hosts ./ansible/deploy.yml"
              sh "echo 'WE ARE DEPLOYING'"
            }
        }
    }

    post {
        always {
            echo 'I will always say Hello again!'
        }
    }
}
