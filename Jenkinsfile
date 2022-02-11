pipeline{ 
    agent any 
    tools {
        maven 'Maven 3.8.4'
        
      }
    stages{
        stage("Checkout"){
            steps{
                echo "========Checking out code========"
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'MyGithubaccount', url: 'https://github.com/Nandhini0610/simple-java-maven-app.git']]])
                
            }
            post{
                success{
                    echo "========Code checkout completed successfully========"
                }
                failure{
                    echo "========Code Checkout failed========"
                }
            }
        }
        stage ("Execute script") {
            steps {
                sh '''
                echo "WORKSPACE: ${WORKSPACE}"
                echo "Jenkins Job Name: ${JOB_NAME}"
                '''
            }
        }
        stage ("Build"){
            steps{
                
                sh "mvn -B -DskipTests clean package"
            }
            }
        stage ("Test"){
            steps{
                
                sh "mvn test"
            }
            post {
                success {
                    junit "target/surefire-reports/**/*.xml"
                }
            }
           
            }
        stage ("sonar scanning") {
            steps {
                script { 
                    def scannerHome = tool name: 'Sonarqube';
                    withSonarQubeEnv("mySonarqubeServer") {
                        sh "${tool("Sonarqube")}/bin/sonar-scanner \
                        -Dsonar.projectKey=Sonarqube \
                        -Dsonar.java.binaries=target \
                        -Dsonar.host.url=http://35.173.133.14:9000/ \
                        -Dsonar.login=b395ceccf7f2b9e33c6e5d9ab769a79ddd80e5cf"
                    }
               }
            }
        }
        stage ("Upload to Nexus") {
            steps {
                sh "mvn -gs ${WORKSPACE}/settings.xml deploy"
               }
            }
        
    }
    post{
        always{
            echo "========always========"
        }
        success{
            echo "========pipeline executed successfully ========"
            archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
        }
        failure{
            echo "========pipeline execution failed========"
        }
    }
}
