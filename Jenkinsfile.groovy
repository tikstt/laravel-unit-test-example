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

        stage ('Unit Testing Stage') {
            steps {
              // Run any testing suites
              sh "./vendor/bin/phpunit --log-junit './reports/unitreport.xml' --coverage-clover './reports/coverage.xml'    "
              junit 'reports/unitreport.xml'
            }
        }

        stage('SonarQube analysis') {
          steps {
              withSonarQubeEnv('sonarcloud.io') {
              sh "/home/ansiblegcp/sonar-scanner-3.2.0.1227-linux/bin/sonar-scanner -Dsonar.php.coverage.reportPaths=./reports/coverage.xml -Dsonar.php.tests.reportPath=./reports/unitreport.xml  -Dsonar.projectKey=tikstt_laravel-unit-test-example -Dsonar.organization=tikstt-github -Dsonar.sources=./app -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=1c2905b8e9ea7825584b3a31280de1ed1903bef3"
              }
              /*
            withSonarQubeEnv('jenkins-sonarqube') {
              sh "${scannerHome}/bin/sonar-scanner"
            }
            */
          }
        }




        stage ('RobotFramework Testing Stage') {
            steps {
                catchError {
                   // sh "pybot ./robotframework/login_tests/valid_login.robot"
                }
                //currentBuild.result = 'SUCCESS'
               // echo currentBuild.result

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
            // junit 'reports/unitreport.xml'
        }
    }
}
