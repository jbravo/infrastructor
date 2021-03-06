package io.infrastructor.core.processing.actions

import org.junit.Test

public class DirectoryActionTest extends ActionTestBase {
    
    @Test
    public void createDirectoryAsRoot() {
        inventory.provision {
            task(filter: {'as:root'}) {
                // execute
                user  name: "testuser"
                group name: "testgroup"
                directory target: '/var/simple', owner: 'testuser', group: 'testgroup', mode: '0600'
                // assert
                def result = shell("ls -dalh /var/simple")
                assert result.output.contains("simple")
                assert result.output.contains("testuser testgroup")
                assert result.output.contains("drw------")
            }
        }
    }
    
    @Test
    public void createDirectoryAsDevopsWithSudo() {
        inventory.provision {
            task(filter: {'as:devops'}) {
                // execute
                directory sudo: true, target: '/etc/simple', owner: 'devops', group: 'devops', mode: '0600'
                // assert
                def result = shell("ls -dalh /etc/simple")
                assert result.output.contains("simple")
                assert result.output.contains("devops devops")
                assert result.output.contains("drw------")
            }
        }
    }
    
    @Test
    public void createNestedDirectories() {
        inventory.provision {
            task(filter: {'as:root'}) {
                // execute
                directory target: '/etc/simple/deep', mode: '600'
                
                def resultDeep = shell 'ls -ldah /etc/simple/deep'
                // assert
                assert resultDeep.exitcode == 0
                assert resultDeep.output.contains('deep')
                assert resultDeep.output.contains("root root")
                assert resultDeep.output.contains("drw-------")
                
                def resultSimple = shell 'ls -ldah /etc/simple'
                // assert
                assert resultSimple.exitcode == 0
                assert resultSimple.output.contains('simple')
                assert resultSimple.output.contains("root root")
                assert resultSimple.output.contains("drwxr-xr-x")
                
                def resultEtc = shell 'ls -ldah /etc'
                // assert
                assert resultEtc.exitcode == 0
                assert resultEtc.output.contains('etc')
                assert resultEtc.output.contains("root root")
                assert resultEtc.output.contains("drwxr-xr-x")
            }
        }
    }

    @Test
    public void createDirectoryAsDevopsWithoutSudo() {
        inventory.provision {
            task(filter: {'as:devops'}) {
                // execute
                def result = directory target: '/etc/simple'
                // assert
                assert result.exitcode != 0
            }
        }
    }
    
    @Test
    public void createDirectoryWithUnknownOwner() {
        inventory.provision {
            task(filter: {'as:root'}) {
                // execute
                def result = directory target: '/etc/simple', owner: 'doesnotexist'
                
                // assert
                assert result.exitcode != 0
            }
        }
    }
 
    @Test
    public void createDirectoryWithUnknownGroup() {
        inventory.provision {
            task(filter: {'as:root'}) {
                // execute
                def result = directory target: '/etc/simple', group: 'doesnotexist'
                
                // assert
                assert result.exitcode != 0
            }
        }
    }
    
    @Test
    public void createDirectoryWithInvalidMode() {
        inventory.provision {
            task(filter: {'as:root'}) {
                // execute
                def result = directory target: '/etc/simple', mode: '8888'
                
                // assert
                assert result.exitcode != 0
            }
        }
    }
}

