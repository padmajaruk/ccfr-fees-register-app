#!groovy

@Library("Reform")
import uk.gov.hmcts.Ansible
import uk.gov.hmcts.Packager

def packager = new Packager(this, 'cc')
def ansible = new Ansible(this, 'ccfr')

def server = Artifactory.server 'artifactory.reform'
def rtMaven = Artifactory.newMavenBuild()
def buildInfo = Artifactory.newBuildInfo()

properties(
    [[$class: 'GithubProjectProperty', displayName: 'Fees Register API', projectUrlStr: 'https://git.reform.hmcts.net/fees-register/fees-register-app'],
    pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)


stageWithNotification('Checkout') {
    deleteDir()
    checkout scm
}

stageWithNotification('Build') {
    rtMaven.tool = 'apache-maven-3.3.9'
    rtMaven.deployer releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
    rtMaven.run pom: 'pom.xml', goals: 'clean install', buildInfo: buildInfo
    archiveArtifacts 'target/*.jar'
}


ifMaster {
    def rpmVersion

    stageWithNotification('Publish JAR') {
        server.publishBuildInfo buildInfo
    }

    stageWithNotification("Publish RPM") {
        rpmVersion = packager.javaRPM('master', 'fees-register-api', '$(ls target/fees-register-api-*.jar)', 'springboot', 'src/main/resources/application.properties')
        packager.publishJavaRPM('fees-register-api')
    }

    stageWithNotification('Deploy') {
        def version = "{fees_register_api_version: ${rpmVersion}}"
        ansible.runDeployPlaybook(version, 'dev')
    }
}

private ifMaster(Closure body) {
    if ("master" == "${env.BRANCH_NAME}") {
        body()
    }
}

private stageWithNotification(String name, Closure body) {
    stage(name) {
        node {
            try {
                body()
            } catch (err) {
                notifyBuildFailure channel: '#cc_tech'
                throw err
            }
        }
    }
}
