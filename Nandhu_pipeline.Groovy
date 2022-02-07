pipeline{
    agent any
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
        stage(sonarqube){
            steps{
                   withSonarQubeEnv(credentialsId: '9e985916-5272-4206-805b-246104b10579') {
    tool name: 'Nandhu_sonarqube', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
sh '''sonar.host.url=http://3.84.39.194:9000/
sonar.login=d78b1bab4fe44e7bf44e6f996bccca710934bd3c
sonar.projectKey=Nandhu_sonarqube
sonar.java.binaries=target'''
}
      
                }  
            }
            post{
                success{
                    echo 'code Test successfull'
                }
                failure{
                    echo 'code Test Failed'
                }
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
