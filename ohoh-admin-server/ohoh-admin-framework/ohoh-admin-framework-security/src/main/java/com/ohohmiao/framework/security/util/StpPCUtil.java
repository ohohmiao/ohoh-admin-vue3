package com.ohohmiao.framework.security.util;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpLogic;
import com.ohohmiao.framework.security.enums.AuthConstEnum;
import com.ohohmiao.framework.security.enums.AuthDeviceEnum;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;

import java.util.Collection;
import java.util.List;

/**
 * pc登录相关核心工具类
 * 重写sa-token原生StpUtil类，建立pc登录账号体系
 *
 * @author ohohmiao
 * @date 2023/4/5 19:20
 */
public class StpPCUtil {

    /**
     * 账号体系标识
     */
    public static final String TYPE = AuthDeviceEnum.PC.getType();

    public static StpLogic stpLogic = new StpLogic(TYPE);

    private StpPCUtil() {

    }

    /**
     * 获取当前登录用户
     * @return 当前登录用户
     */
    public static StpLoginUser getLoginUser() {
        return (StpLoginUser) StpPCUtil.getTokenSession().get(AuthConstEnum.LOGINUSER_SESSIONKEY.getValue());
    }

    /**
     * 是否在数据权限范围内
     * @param orgId 组织id
     * @return 结果
     */
    public static boolean isAuthDataScope(String orgId){
        StpLoginUser loginUser = getLoginUser();
        if(loginUser.isSuperAdmin()){
            return true;
        }else{
            return loginUser.getGrantedDataScopes().contains(orgId);
        }
    }

    /**
     * 是否在数据权限范围内
     * @param orgIds 组织id集合
     * @return 结果
     */
    public static boolean isAuthDataScopes(Collection<String> orgIds){
        StpLoginUser loginUser = getLoginUser();
        if(loginUser.isSuperAdmin()){
            return true;
        }else{
            boolean isAllGranted = true;
            for(String orgId: orgIds){
                if(!loginUser.getGrantedDataScopes().contains(orgId)){
                    isAllGranted = false;
                    break;
                }
            }
            return isAllGranted;
        }
    }

    /**
     * 是否在任一数据权限范围内
     * @param orgIds 组织id集合
     * @return 结果
     */
    public static boolean isAuthAnyDataScopes(Collection<String> orgIds){
        StpLoginUser loginUser = getLoginUser();
        if(loginUser.isSuperAdmin()){
            return true;
        }else{
            boolean isAnyGranted = false;
            for(String orgId: orgIds){
                if(loginUser.getGrantedDataScopes().contains(orgId)){
                    isAnyGranted = true;
                    break;
                }
            }
            return isAnyGranted;
        }
    }

    /**
     * 是否有被授权角色
     * @param roleId 角色id
     * @return 结果
     */
    public static boolean isAuthRole(String roleId){
        StpLoginUser loginUser = getLoginUser();
        if(loginUser.isSuperAdmin()){
            return true;
        }else{
            return loginUser.getGrantedRoleIds().contains(roleId);
        }
    }

    /**
     * 是否有被授权角色集合
     * @param roleIds 角色id集合
     * @return 结果
     */
    public static boolean isAuthRoles(List<String> roleIds){
        StpLoginUser loginUser = getLoginUser();
        if(loginUser.isSuperAdmin()){
            return true;
        }else{
            boolean isAllGranted = true;
            for(String roleId: roleIds){
                if(!loginUser.getGrantedRoleIds().contains(roleId)){
                    isAllGranted = false;
                    break;
                }
            }
            return isAllGranted;
        }
    }

    public static String getLoginType() {
        return stpLogic.getLoginType();
    }

    public static void setStpLogic(StpLogic newStpLogic) {
        stpLogic = newStpLogic;
        SaManager.putStpLogic(newStpLogic);
    }

    public static StpLogic getStpLogic() {
        return stpLogic;
    }

    public static String getTokenName() {
        return stpLogic.getTokenName();
    }

    public static void setTokenValue(String tokenValue) {
        stpLogic.setTokenValue(tokenValue);
    }

    public static void setTokenValue(String tokenValue, int cookieTimeout) {
        stpLogic.setTokenValue(tokenValue, cookieTimeout);
    }

    public static String getTokenValue() {
        return stpLogic.getTokenValue();
    }

    public static String getTokenValueNotCut() {
        return stpLogic.getTokenValueNotCut();
    }

    public static SaTokenInfo getTokenInfo() {
        return stpLogic.getTokenInfo();
    }

    public static void login(Object id) {
        stpLogic.login(id);
    }

    public static void login(Object id, String device) {
        stpLogic.login(id, device);
    }

    public static void login(Object id, boolean isLastingCookie) {
        stpLogic.login(id, isLastingCookie);
    }

    public static void login(Object id, long timeout) {
        stpLogic.login(id, timeout);
    }

    public static void login(Object id, SaLoginModel loginModel) {
        stpLogic.login(id, loginModel);
    }

    public static String createLoginSession(Object id) {
        return stpLogic.createLoginSession(id);
    }

    public static String createLoginSession(Object id, SaLoginModel loginModel) {
        return stpLogic.createLoginSession(id, loginModel);
    }

    public static void logout() {
        stpLogic.logout();
    }

    public static void logout(Object loginId) {
        stpLogic.logout(loginId);
    }

    public static void logout(Object loginId, String device) {
        stpLogic.logout(loginId, device);
    }

    public static void logoutByTokenValue(String tokenValue) {
        stpLogic.logoutByTokenValue(tokenValue);
    }

    public static void kickout(Object loginId) {
        stpLogic.kickout(loginId);
    }

    public static void kickout(Object loginId, String device) {
        stpLogic.kickout(loginId, device);
    }

    public static void kickoutByTokenValue(String tokenValue) {
        stpLogic.kickoutByTokenValue(tokenValue);
    }

    public static void replaced(Object loginId, String device) {
        stpLogic.replaced(loginId, device);
    }

    public static boolean isLogin() {
        return stpLogic.isLogin();
    }

    public static void checkLogin() {
        stpLogic.checkLogin();
    }

    public static Object getLoginId() {
        return stpLogic.getLoginId();
    }

    public static <T> T getLoginId(T defaultValue) {
        return stpLogic.getLoginId(defaultValue);
    }

    public static Object getLoginIdDefaultNull() {
        return stpLogic.getLoginIdDefaultNull();
    }

    public static String getLoginIdAsString() {
        return stpLogic.getLoginIdAsString();
    }

    public static int getLoginIdAsInt() {
        return stpLogic.getLoginIdAsInt();
    }

    public static long getLoginIdAsLong() {
        return stpLogic.getLoginIdAsLong();
    }

    public static Object getLoginIdByToken(String tokenValue) {
        return stpLogic.getLoginIdByToken(tokenValue);
    }

    public static Object getExtra(String key) {
        return stpLogic.getExtra(key);
    }

    public static Object getExtra(String tokenValue, String key) {
        return stpLogic.getExtra(tokenValue, key);
    }

    public static SaSession getSessionByLoginId(Object loginId, boolean isCreate) {
        return stpLogic.getSessionByLoginId(loginId, isCreate);
    }

    public static SaSession getSessionBySessionId(String sessionId) {
        return stpLogic.getSessionBySessionId(sessionId);
    }

    public static SaSession getSessionByLoginId(Object loginId) {
        return stpLogic.getSessionByLoginId(loginId);
    }

    public static SaSession getSession(boolean isCreate) {
        return stpLogic.getSession(isCreate);
    }

    public static SaSession getSession() {
        return stpLogic.getSession();
    }

    public static SaSession getTokenSessionByToken(String tokenValue) {
        return stpLogic.getTokenSessionByToken(tokenValue);
    }

    public static SaSession getTokenSession() {
        return stpLogic.getTokenSession();
    }

    public static SaSession getAnonTokenSession() {
        return stpLogic.getAnonTokenSession();
    }

    public static long getTokenTimeout() {
        return stpLogic.getTokenTimeout();
    }

    public static long getSessionTimeout() {
        return stpLogic.getSessionTimeout();
    }

    public static long getTokenSessionTimeout() {
        return stpLogic.getTokenSessionTimeout();
    }

    public static void renewTimeout(long timeout) {
        stpLogic.renewTimeout(timeout);
    }

    public static void renewTimeout(String tokenValue, long timeout) {
        stpLogic.renewTimeout(tokenValue, timeout);
    }

    public static List<String> getRoleList() {
        return stpLogic.getRoleList();
    }

    public static List<String> getRoleList(Object loginId) {
        return stpLogic.getRoleList(loginId);
    }

    public static boolean hasRole(String role) {
        return stpLogic.hasRole(role);
    }

    public static boolean hasRole(Object loginId, String role) {
        return stpLogic.hasRole(loginId, role);
    }

    public static boolean hasRoleAnd(String... roleArray) {
        return stpLogic.hasRoleAnd(roleArray);
    }

    public static boolean hasRoleOr(String... roleArray) {
        return stpLogic.hasRoleOr(roleArray);
    }

    public static void checkRole(String role) {
        stpLogic.checkRole(role);
    }

    public static void checkRoleAnd(String... roleArray) {
        stpLogic.checkRoleAnd(roleArray);
    }

    public static void checkRoleOr(String... roleArray) {
        stpLogic.checkRoleOr(roleArray);
    }

    public static List<String> getPermissionList() {
        return stpLogic.getPermissionList();
    }

    public static List<String> getPermissionList(Object loginId) {
        return stpLogic.getPermissionList(loginId);
    }

    public static boolean hasPermission(String permission) {
        return stpLogic.hasPermission(permission);
    }

    public static boolean hasPermission(Object loginId, String permission) {
        return stpLogic.hasPermission(loginId, permission);
    }

    public static boolean hasPermissionAnd(String... permissionArray) {
        return stpLogic.hasPermissionAnd(permissionArray);
    }

    public static boolean hasPermissionOr(String... permissionArray) {
        return stpLogic.hasPermissionOr(permissionArray);
    }

    public static void checkPermission(String permission) {
        stpLogic.checkPermission(permission);
    }

    public static void checkPermissionAnd(String... permissionArray) {
        stpLogic.checkPermissionAnd(permissionArray);
    }

    public static void checkPermissionOr(String... permissionArray) {
        stpLogic.checkPermissionOr(permissionArray);
    }

    public static String getTokenValueByLoginId(Object loginId) {
        return stpLogic.getTokenValueByLoginId(loginId);
    }

    public static String getTokenValueByLoginId(Object loginId, String device) {
        return stpLogic.getTokenValueByLoginId(loginId, device);
    }

    public static List<String> getTokenValueListByLoginId(Object loginId) {
        return stpLogic.getTokenValueListByLoginId(loginId);
    }

    public static List<String> getTokenValueListByLoginId(Object loginId, String device) {
        return stpLogic.getTokenValueListByLoginId(loginId, device);
    }

    public static String getLoginDevice() {
        return stpLogic.getLoginDevice();
    }

    public static List<String> searchTokenValue(String keyword, int start, int size, boolean sortType) {
        return stpLogic.searchTokenValue(keyword, start, size, sortType);
    }

    public static List<String> searchSessionId(String keyword, int start, int size, boolean sortType) {
        return stpLogic.searchSessionId(keyword, start, size, sortType);
    }

    public static List<String> searchTokenSessionId(String keyword, int start, int size, boolean sortType) {
        return stpLogic.searchTokenSessionId(keyword, start, size, sortType);
    }

    public static void disable(Object loginId, long time) {
        stpLogic.disable(loginId, time);
    }

    public static boolean isDisable(Object loginId) {
        return stpLogic.isDisable(loginId);
    }

    public static void checkDisable(Object loginId) {
        stpLogic.checkDisable(loginId);
    }

    public static long getDisableTime(Object loginId) {
        return stpLogic.getDisableTime(loginId);
    }

    public static void untieDisable(Object loginId) {
        stpLogic.untieDisable(loginId);
    }

    public static void disable(Object loginId, String service, long time) {
        stpLogic.disable(loginId, service, time);
    }

    public static boolean isDisable(Object loginId, String service) {
        return stpLogic.isDisable(loginId, service);
    }

    public static void checkDisable(Object loginId, String... services) {
        stpLogic.checkDisable(loginId, services);
    }

    public static long getDisableTime(Object loginId, String service) {
        return stpLogic.getDisableTime(loginId, service);
    }

    public static void untieDisable(Object loginId, String... services) {
        stpLogic.untieDisable(loginId, services);
    }

    public static void disableLevel(Object loginId, int level, long time) {
        stpLogic.disableLevel(loginId, level, time);
    }

    public static void disableLevel(Object loginId, String service, int level, long time) {
        stpLogic.disableLevel(loginId, service, level, time);
    }

    public static boolean isDisableLevel(Object loginId, int level) {
        return stpLogic.isDisableLevel(loginId, level);
    }

    public static boolean isDisableLevel(Object loginId, String service, int level) {
        return stpLogic.isDisableLevel(loginId, service, level);
    }

    public static void checkDisableLevel(Object loginId, int level) {
        stpLogic.checkDisableLevel(loginId, level);
    }

    public static void checkDisableLevel(Object loginId, String service, int level) {
        stpLogic.checkDisableLevel(loginId, service, level);
    }

    public static int getDisableLevel(Object loginId) {
        return stpLogic.getDisableLevel(loginId);
    }

    public static int getDisableLevel(Object loginId, String service) {
        return stpLogic.getDisableLevel(loginId, service);
    }

    public static void switchTo(Object loginId) {
        stpLogic.switchTo(loginId);
    }

    public static void endSwitch() {
        stpLogic.endSwitch();
    }

    public static boolean isSwitch() {
        return stpLogic.isSwitch();
    }

    public static void switchTo(Object loginId, SaFunction function) {
        stpLogic.switchTo(loginId, function);
    }

    public static void openSafe(long safeTime) {
        stpLogic.openSafe(safeTime);
    }

    public static boolean isSafe() {
        return stpLogic.isSafe();
    }

    public static void checkSafe() {
        stpLogic.checkSafe();
    }

    public static long getSafeTime() {
        return stpLogic.getSafeTime();
    }

    public static void closeSafe() {
        stpLogic.closeSafe();
    }

}
