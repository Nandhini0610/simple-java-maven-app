pipeline{
    agent{
        label "Mybuildserver"
    }
     tools{
        maven 'Maven 3.8.4' 
     }
     stages{
        stage (code_checkout){
            steps{
                  echo 'Pipeline First Project'
                  checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Nandhini0610/simple-java-maven-app.git']]])
            }
            post{
                success{
                    echo 'code checkout successfull'
                }
                failure{
                    echo 'code checkout Failed'
                }
            }
        }
        stage (build){
            steps{
                  
                  sh 'mvn -DskipTests clean package'
            }
            post{
                success{
                    echo 'Build successfull'
                }
                failure{
                    echo 'Build Failed'
                }
            }
        }
        stage (junit_test){
            steps{
                  
                  sh 'mvn test'
            }
            post{
                success{
                    echo 'Build successfull'
                    junit 'target/surefire-reports/TEST-com.mycompany.app.AppTest.xml	'
                }
                failure{
                    echo 'Build Failed'
                }
            }
       }
     /*stage ("sonar scanning") {
            steps {
                script { 
                    def scannerHome = tool name: 'Sonarqube';
                    withSonarQubeEnv("Sonarqube") {
                        sh "${tool("Sonarqube")}/bin/sonar-scanner \
                        -Dsonar.projectKey=Sonarqube \
                        -Dsonar.java.binaries=target \
                        -Dsonar.host.url=http://52.55.7.132:9000/ \
                        -Dsonar.login=b395ceccf7f2b9e33c6e5d9ab769a79ddd80e5cf"
                    }
               }
            }
        } */
        stage ("Upload to Nexus") {
            steps {
                sh "mvn -gs ${WORKSPACE}/settings.xml deploy"
               }
            }
        stage (execute_shell){
            steps{
                echo "Buildid : $BUILD_ID"
                echo "jobname : $JOB_NAME"
                echo "workspace : $WORKSPACE"
                echo "Jenkinsname : $JENKINS_HOME"
                echo "$Parameter"
            }
        } 
     }
     post{
            always{
                    echo 'Post action'
                }
                success{
                    echo 'Pipeline Project successfull'
                    archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
                }
                failure{
                    echo 'Pipeline Project Failed'
                }
    }
}
    
    
