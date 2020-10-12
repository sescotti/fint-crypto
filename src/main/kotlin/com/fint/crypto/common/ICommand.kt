package com.fint.crypto.common

interface ICommand<FROM, TO> {

    fun execute(subject: FROM): TO

}
