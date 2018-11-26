pipeline {
    agent any

    stages {

        stage ('Git  Clone') {
            steps {
              git url: 'https://github.com/tikstt/laravel-unit-test-example.git/'
            }
        }

        stage ('Compile Stage') {
            steps {
              sh "cp .env.example .env"
              sh "composer install --prefer-dist"
            }
        }
/*
        stage('SonarQube analysis') {
          steps {
            // requires SonarQube Scanner 2.8+
            //def scannerHome = tool 'SonarQube Scanner 2.8';
            withSonarQubeEnv('jenkins-sonarqube') {
              sh "${scannerHome}/bin/sonar-scanner"
            }
          }
        }
*/

        stage ('Unit Testing Stage') {
            steps {
              // Run any testing suites
              sh "./vendor/bin/phpunit --log-junit 'reports/unitreport.xml'  "
            }
        }
        
        stage ('Load Test Stage') {
            steps {
              // Run any testing suites
              sh "jmeter -n -t ./jmeter/kbtg.jmx -l ./jmeter/kbtg_result.jtl  -Jjmeter.save.saveservice.output_format=xml"
              performanceReport parsers: [[$class: 'JMeterParser']],sourceDataFiles:"./jmeter/kbtg_result.jtl"
              //performanceReport parsers: [[$class: 'JMeterParser', glob: "./jmeter/kbtg_result.jtl"]], errorFailedThreshold: 1, errorUnstableThreshold: 1, ignoreFailedBuild: false, ignoreUnstableBuild: false, relativeFailedThresholdNegative: 0, relativeFailedThresholdPositive: 0, relativeUnstableThresholdNegative: 0, relativeUnstableThresholdPositive: 0
            }
            //steps([$class: 'ArtifactArchiver', artifacts: './jmeter/kbtg_result.jtl'])
        }        
        
        /*
        ./vendor/bin/phpunit --log-junit 'reports/unitreport.xml'  --coverage-html 'reports/coverage' --coverage-clover 'reports/coverage/coverage.xml'
        */

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
             junit 'reports/unitreport.xml'
        }
    }
}
