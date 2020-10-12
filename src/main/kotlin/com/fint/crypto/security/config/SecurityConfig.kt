package com.fint.crypto.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@EnableWebSecurity
class SecurityConfig(private val properties: SecurityProperties): WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(*properties.excludedPaths.toTypedArray())
    }

    override fun configure(http: HttpSecurity) {
        http.exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(UNAUTHORIZED))
    }

    @Configuration
    @ConfigurationProperties(prefix = "crypto.security")
    class SecurityProperties {
        lateinit var excludedPaths: Set<String>
    }

}
