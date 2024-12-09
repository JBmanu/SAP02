tasks.register("runAllShadowJar") {
    description = "Esegue task specifici per tutti i moduli dinamicamente"
    group = "Custom"

    subprojects.forEach { subproject ->
        dependsOn("${subproject.path}:shadowJar")
    }
}