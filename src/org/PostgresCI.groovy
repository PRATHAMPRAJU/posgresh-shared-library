package org

class PostgresCI {
    static void runPipeline(pipelineContext) {
        pipelineContext.pipeline {
            agent any // Correct use of the agent directive
            stages {
                stage('Clone Repository') {
                    steps {
                        git url: 'https://github.com/PRATHAMPRAJU/Postgresql-Role.git', branch: 'main'
                    }
                }
                stage('Run Ansible Lint') {
                    steps {
                        sh 'ansible-lint'
                    }
                }
                stage('Test Role') {
                    steps {
                        sh '''
                        ansible-playbook -i aws_ec2.yaml playbook.yml --private-key=/home/ubuntu/key-pair.pem --syntax-check
                        '''
                    }
                }
                stage('Post Results') {
                    steps {
                        archiveArtifacts artifacts: '**/reports/*', allowEmptyArchive: true
                    }
                }
            }
        }
    }
}
