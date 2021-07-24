/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.vcs.internal

import org.gradle.integtests.fixtures.AbstractIntegrationSpec
import org.gradle.vcs.fixtures.GitFileRepository
import org.junit.Rule

class OfflineFileSourceDependencyIntegrationTest extends AbstractIntegrationSpec {
    @Rule
    GitFileRepository repo = new GitFileRepository('dep', temporaryFolder.getTestDirectory())

    def setup() {
        settingsFile << """
            rootProject.name = 'consumer'
            gradle.rootProject {
                configurations {
                    compile
                }
                dependencies {
                    compile 'test:test:1.2'
                }
                tasks.register('resolve') {
                    inputs.files configurations.compile
                    doLast { configurations.compile.each { } }
                }
            }
            sourceControl.vcsMappings.withModule('test:test') {
                from(GitVersionControlSpec) {
                    url = uri('${repo.url}')
                }
            }
        """

        repo.file('settings.gradle') << """
            rootProject.name = 'test'
            gradle.rootProject {
                group = 'test'
                configurations {
                    create('default')
                }
            }
        """
        repo.commit('initial version')
        repo.createLightWeightTag('1.2')
    }

    def "can checkout offline repositories successfully when offline"() {
        when:
        executer.withArgument('--offline')
        succeeds('resolve')

        then:
        noExceptionThrown()
    }
}
