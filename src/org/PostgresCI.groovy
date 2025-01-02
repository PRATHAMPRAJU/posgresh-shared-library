package org

class PostgresCI {
    static void runPipeline(pipelineContext) {
        pipeline {
            agent any
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
                        ansible-playbook -i /opt/aws_ec2.yaml playbook.yml --private-key=/home/ubuntu/key-pair.pem --syntax-check
                        ansible-playbook -i /opt/aws_ec2.yaml playbook.yml --private-key=/home/ubuntu/key-pair.pem
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
