package com.fint.crypto.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfig(private val properties: SecurityProperties): WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(*properties.excludedPaths.toTypedArray())
    }

    @Configuration
    @ConfigurationProperties(prefix = "crypto.security")
    class SecurityProperties {
        lateinit var excludedPaths: Set<String>
    }

}
