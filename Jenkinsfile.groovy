pipeline {
    agent any

    stages {
    
        stage ('Compile Stage') {
            sh "cp .env.example .env"
            sh "composer install --prefer-dist"
            
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
