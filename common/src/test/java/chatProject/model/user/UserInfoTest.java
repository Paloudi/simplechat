package chatProject.model.user;

import chatProject.model.FakeInstances;
import org.junit.Assert;
import org.junit.Test;

public class UserInfoTest {

    @Test
    public void setCurrentStatus() {
        UserInfo userInfo = FakeInstances.DUMMY_ACTIVE_USER;
        userInfo.setCurrentStatus(Status.INACTIVE);
        Assert.assertEquals(Status.INACTIVE, userInfo.getCurrentStatus());
    }
}
