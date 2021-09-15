package com.looca.service;

import com.spring.annotation.Component;
import com.spring.annotation.Scope;
import com.spring.tool.CommonConstant;

/**
 * 模拟@Scope为Prototype即原型(多例)bean
 * @author looca
 */
@Component()
@Scope(CommonConstant.SCOPE_PROTOTYPE)
public class PrototypeScopeService {
}
