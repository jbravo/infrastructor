package io.infrastructor.core.tasks

import org.testng.annotations.Test


public class ReplaceActionTest extends TaskTestBase {
    
    @Test
    public void replaceAllOccurrencesInFileUsingRegex() {
        inventory.setup {
            nodes('as:root') {
                file {
                    target  = '/test.txt'
                    content = """\
                    line 1
                    line 2
                    """
                }
                
                replace {
                    target  = '/test.txt'
                    regexp  = /(?m)line/
                    content = "another"
                    all     = true
                }       
                
                assert shell("cat /test.txt").output == """\
                another 1
                another 2
                """.stripMargin().stripIndent()
            }
        }
    }
    
    @Test
    public void replaceFirstOccurrenceInFileUsingRegex() {
        inventory.setup {
            nodes('as:root') {
                file {
                    target = '/test.txt'
                    content = """\
                    line 1
                    line 2
                    """
                }
                
                replace {
                    target  = '/test.txt'
                    regexp  = /(?m)line/
                    content = "another"
                    all     = false
                } 
                        
                assert shell("cat /test.txt").output == """\
                another 1
                line 2
                """.stripMargin().stripIndent()
            }
        }
    }
    
    @Test
    public void replaceBlockWithUnknownOwner() {
        inventory.setup {
            nodes('as:devops') {
                file target: '/tmp/test.txt', content: "dummy"
                
                def result = replace {
                    target  = '/tmp/test.txt'
                    regexp  = /dummy/
                    content = "another"
                    owner = 'unknown'
                } 
                
                assert result.exitcode != 0
                assert result.error.find(/invalid spec/)
            }
        }
    }
    
    @Test
    public void replaceBlockWithUnknownGroup() {
        inventory.setup {
            nodes('as:devops') {
                file target: '/tmp/test.txt', content: "dummy"
                
                def result = replace {
                    target  = '/tmp/test.txt'
                    regexp  = /dummy/
                    content = "another"
                    group = 'unknown'
                }
                
                assert result.exitcode != 0
                assert result.error.find(/invalid group/)
            }
        }
    }
    
    @Test
    public void replaceBlockWithInvalidMode() {
        inventory.setup {
            nodes('as:devops') {
                file target: '/tmp/test.txt', content: "dummy"
                
                def result = replace {
                    target  = '/tmp/test.txt'
                    regexp  = /dummy/
                    content = "another"
                    mode = '888'
                }
                
                assert result.exitcode != 0
                assert result.error.find(/invalid mode/)
            }
        }
    }
}

