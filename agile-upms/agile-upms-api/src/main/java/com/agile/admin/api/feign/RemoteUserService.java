package com.agile.admin.api.feign;

import com.agile.admin.api.dto.UserDTO;
import com.agile.admin.api.dto.UserInfo;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.constant.ServiceNameConstants;
import com.agile.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Remote user service.
 *
 * @author Huang Z.Y.
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteUserService {

    /**
     * Query information about users and roles by user name.
     *
     * @param user User query object
     * @param from Call flag
     * @return R
     */
    @GetMapping("/user/info/query")
    R<UserInfo> info(@SpringQueryMap UserDTO user, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * Lock user.
     *
     * @param username Username
     * @param from     Call flag
     */
    @PutMapping("/user/lock/{username}")
    R<Boolean> lockUser(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM) String from);
    
}
