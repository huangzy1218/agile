package com.agile.common.core.exception;

/**
 * Error codes.
 *
 * @author Huang Z.Y.
 */
public interface ErrorCodes {

    /**
     * System parameter configuration error
     */
    String SYS_PARAM_CONFIG_ERROR = "sys.param.config.error";

    /**
     * System built-in parameters cannot be deleted
     */
    String SYS_PARAM_DELETE_SYSTEM = "sys.param.delete.system";

    /**
     * User already exists
     */
    String SYS_USER_EXISTING = "sys.user.existing";

    /**
     * Username already exists
     */
    String SYS_USER_USERNAME_EXISTING = "sys.user.username.existing";

    /**
     * Incorrect original password, failed to update
     */
    String SYS_USER_UPDATE_PASSWORDERROR = "sys.user.update.passwordError";

    /**
     * User information is empty
     */
    String SYS_USER_USERINFO_EMPTY = "sys.user.userInfo.empty";

    /**
     * Failed to get current user information
     */
    String SYS_USER_QUERY_ERROR = "sys.user.query.error";

    /**
     * Department name does not exist
     */
    String SYS_DEPT_DEPTNAME_INEXISTENCE = "sys.dept.deptName.inexistence";

    /**
     * Post name does not exist
     */
    String SYS_POST_POSTNAME_INEXISTENCE = "sys.post.postName.inexistence";

    /**
     * Post name or code already exists
     */
    String SYS_POST_NAMEORCODE_EXISTING = "sys.post.nameOrCode.existing";

    /**
     * Role name does not exist
     */
    String SYS_ROLE_ROLENAME_INEXISTENCE = "sys.role.roleName.inexistence";

    /**
     * Role name or code already exists
     */
    String SYS_ROLE_NAMEORCODE_EXISTING = "sys.role.nameOrCode.existing";

    /**
     * Menu has sub-nodes, deletion failed
     */
    String SYS_MENU_DELETE_EXISTING = "sys.menu.delete.existing";

    /**
     * System built-in dictionary cannot be deleted
     */
    String SYS_DICT_DELETE_SYSTEM = "sys.dict.delete.system";

    /**
     * System built-in dictionary cannot be updated
     */
    String SYS_DICT_UPDATE_SYSTEM = "sys.dict.update.system";

    /**
     * Verification code sending too frequently
     */
    String SYS_APP_SMS_OFTEN = "sys.app.sms.often";

    /**
     * Incorrect verification code
     */
    String SYS_APP_SMS_ERROR = "sys.app.sms.error";

    /**
     * Phone number not registered
     */
    String SYS_APP_PHONE_UNREGISTERED = "sys.app.phone.unregistered";

}
