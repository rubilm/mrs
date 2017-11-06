#!/usr/bin/env groovy

pipeline {
	agent any
	
	stages {
		stage('Initialize') {
			steps {
				echo 'Initializing....'
				git url: 'https://github.com/rubilm/mrs.git', branch: 'develop'
			}
		}
		stage('Compile & Test') {
			steps {
				echo 'running Maven build'
			}
			post {
				success {
					echo 'done.'
				}
			}
		}
		stage('System Tests') {
			steps {
				echo 'running Docker'
			}
		}
	}
}
