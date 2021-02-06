package ru.otus.otuskotlin.remindercalendar.common.mp

class ChildValidator : Validator<Child> {
    private val nameValidator = NameValidator()
    private val ageValidator = AgeValidator(0, 18)
    override fun validate(child: Child) = ValidationResult(
        nameValidator.validate(child.name), ageValidator.validate(child.age)
    )
}