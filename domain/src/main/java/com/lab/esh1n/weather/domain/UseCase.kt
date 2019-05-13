package com.lab.esh1n.weather.domain

interface UseCase<ARG, RESULT> {
   suspend fun execute(argument: ARG) : RESULT
}