package com.shawntime.common.common.spelkey;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

/**
 * Created by IDEA
 * User: mashaohua
 * Date: 2016-10-12 10:06
 * Desc:
 */
class KeySpELEvaluationContext extends StandardEvaluationContext {

    private final ParameterNameDiscoverer paramDiscoverer;

    private final Method method;

    private final Object[] args;

    private final Class<?> targetClass;

    private final Map<MethodCacheKey, Method> methodCache;

    private final List<String> unavailableVariables;

    private boolean paramLoaded = false;


    KeySpELEvaluationContext(Object rootObject, ParameterNameDiscoverer paramDiscoverer, Method method,
                             Object[] args, Class<?> targetClass, Map<MethodCacheKey, Method> methodCache) {
        super(rootObject);

        this.paramDiscoverer = paramDiscoverer;
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
        this.methodCache = methodCache;
        this.unavailableVariables = new ArrayList<String>();
    }

    /**
     * Add the specified variable name as unavailable for that context. Any expression trying
     * to access this variable should lead to an exception.
     * <p>This permits the validation of expressions that could potentially a variable even
     * when such variable isn't available yet. Any expression trying to use that variable should
     * therefore fail to evaluate.
     */
    public void addUnavailableVariable(String name) {
        this.unavailableVariables.add(name);
    }


    /**
     * Load the param information only when needed.
     */
    @Override
    public Object lookupVariable(String name) {
        if (this.unavailableVariables.contains(name)) {
            throw new VariableNotAvailableException(name);
        }
        Object variable = super.lookupVariable(name);
        if (variable != null) {
            return variable;
        }
        if (!this.paramLoaded) {
            loadArgsAsVariables();
            this.paramLoaded = true;
            variable = super.lookupVariable(name);
        }
        return variable;
    }

    private void loadArgsAsVariables() {
        // shortcut if no args need to be loaded
        if (ObjectUtils.isEmpty(this.args)) {
            return;
        }

        MethodCacheKey methodKey = new MethodCacheKey(this.method, this.targetClass);
        Method targetMethod = this.methodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(this.method, this.targetClass);
            if (targetMethod == null) {
                targetMethod = this.method;
            }
            this.methodCache.put(methodKey, targetMethod);
        }

        // save arguments as indexed variables
        for (int i = 0; i < this.args.length; i++) {
            setVariable("a" + i, this.args[i]);
            setVariable("p" + i, this.args[i]);
        }

        String[] parameterNames = this.paramDiscoverer.getParameterNames(targetMethod);
        // save parameter names (if discovered)
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                setVariable(parameterNames[i], this.args[i]);
            }
        }
    }

}
