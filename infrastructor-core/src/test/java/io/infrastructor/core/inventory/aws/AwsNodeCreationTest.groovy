package io.infrastructor.core.inventory.aws

import org.junit.Test
import org.junit.experimental.categories.Category

import static io.infrastructor.core.inventory.aws.managed.ManagedAwsInventory.managedAwsInventory
import static io.infrastructor.core.inventory.aws.AwsInventory.awsInventory

@Category(AwsCategory.class)
public class AwsNodeCreationTest extends AwsTestBase {
    
    @Test
    public void rebuildNodeWhenDiskSizehasChanged() {
        try {
            
            def initialInventory = managedAwsInventory(AWS_ACCESS_KEY_ID, AWS_ACCESS_SECRET_KEY, AWS_REGION) {
                ec2(tags: [managed: true], usePublicIp: true) {
                    node {
                        name = 'simple-y'
                        imageId = 'ami-3f1bd150' // Ubuntu Server 16.04 LTS (HVM), SSD Volume Type
                        instanceType = 't2.micro'
                        subnetId = 'subnet-fd7b3b95' // EU Centra-1, default VPC with public IPs
                        keyName = 'aws_infrastructor_ci'
                        securityGroupIds = ['sg-8e6fcae5'] // default-ssh-only
                        username = "ubuntu"
                        keyfile = "resources/aws/aws_infrastructor_ci"
                        blockDeviceMapping {
                            name = '/dev/sda1'
                            deleteOnTermination = true
                            volumeSize = 20
                            volumeType = 'gp2'
                        }
                    }
                }
            }
            
            initialInventory.provision {}  
            assert initialInventory.nodes.size() == 1
            assert initialInventory.nodes[0].state == 'created'
            
            def updatedInventory = managedAwsInventory(AWS_ACCESS_KEY_ID, AWS_ACCESS_SECRET_KEY, AWS_REGION) {
                ec2(tags: [managed: true], usePublicIp: true) {
                    node {
                        name = 'simple-y'
                        imageId = 'ami-3f1bd150' // Ubuntu Server 16.04 LTS (HVM), SSD Volume Type
                        instanceType = 't2.micro'
                        subnetId = 'subnet-fd7b3b95' // EU Centra-1, default VPC with public IPs
                        keyName = 'aws_infrastructor_ci'
                        securityGroupIds = ['sg-8e6fcae5'] // default-ssh-only
                        username = "ubuntu"
                        keyfile = "resources/aws/aws_infrastructor_ci"
                        blockDeviceMapping {
                            name = '/dev/sda1'
                            deleteOnTermination = true
                            volumeSize = 16
                            volumeType = 'gp2'
                        }
                    }
                }
            }
            
            updatedInventory.provision {} 
            assert updatedInventory.nodes.size() == 2
            assert updatedInventory.nodes.find { it.state == 'created' }
            assert updatedInventory.nodes.find { it.state == 'removed' }
            
        } finally {
            managedAwsInventory(AWS_ACCESS_KEY_ID, AWS_ACCESS_SECRET_KEY, AWS_REGION) { 
                ec2(tags: [managed: true], usePublicIp: true, username: 'ubuntu', keyfile: 'resources/aws/aws_infrastructor_ci') {} 
            }.provision {}
        }
    }
}

