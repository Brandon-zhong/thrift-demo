package com.lupf.thriftclient.service;

import com.iyingdi.user.thrift.UserDTO;
import com.iyingdi.user.thrift.UserThriftService;
import org.apache.thrift.TException;

/**
 * @author brandon
 * create on 2020-07-09
 * desc:
 */
public class UserService implements UserThriftService.Iface {

    @Override
    public UserDTO getUserByUserId(int userId) throws TException {
        return null;
    }
}
