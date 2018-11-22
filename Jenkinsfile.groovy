pipeline {
    agent any

    stages {
        stage('git clone') {       
          git clone "https://github.com/tikstt/laravel-unit-test-example.git"
        }    
    
        stage ('Compile Stage') {
            sh "composer install"
            sh "cp .env.example .env"
        }

        stage ('Testing Stage') {
            // Run any testing suites
            sh "./vendor/bin/phpunit"
        }


        stage ('Deployment Stage') {
            // If we had ansible installed on the server, setup to run an ansible playbook
            // sh "ansible-playbook -i ./ansible/hosts ./ansible/deploy.yml"
            sh "echo 'WE ARE DEPLOYING'"
        }
    }
    
    post { 
        always { 
            echo 'I will always say Hello again!'
        }
    }    
}
