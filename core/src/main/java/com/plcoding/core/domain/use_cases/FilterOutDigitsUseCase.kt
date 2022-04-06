package com.plcoding.core.domain.use_cases

class FilterOutDigitsUseCase {

    operator fun invoke(text: String) : String{

        return text.filter { it.isDigit() }
    }
}