#!/usr/bin/env groovy

pipeline {
	agent any
	
	stages {
		
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
