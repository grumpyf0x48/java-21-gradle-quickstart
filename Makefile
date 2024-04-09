help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' Makefile | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

update-application: update-files update-java-files ## Update the application

update-files: build.gradle.kts settings.gradle.kts .github/workflows/build.yml .github/workflows/native_build.yml ## Update all build files (Gradle and GitHub actions files)
	@for file in $^; do \
		FILE_NAME="$${file}" make update-file; \
	done

update-java-files: $(shell find . -name "*.java") ## Update all Java files
	@for file in $^; do \
		FILE_NAME="$${file}" make update-java-file; \
	done

update-file: check ## Update a build file (Gradle and GitHub actions files)
	@sed -i -e "s#$(INITIAL_APPLICATION_NAME)#$(UPDATED_APPLICATION_NAME)#g" -e "s#$(INITIAL_PACKAGE_NAME)#$(PACKAGE_NAME)#" $(FILE_NAME)
	@git add $(FILE_NAME)

update-java-file: check ## Update a Java file
	@mkdir -p $(shell dirname $(UPDATED_FILE_NAME))
	@sed -e "s#$(INITIAL_APPLICATION_NAME)#$(UPDATED_APPLICATION_NAME)#" -e "s#$(INITIAL_PACKAGE_NAME)#$(PACKAGE_NAME)#" $(FILE_NAME) > $(UPDATED_FILE_NAME)
	@git rm $(FILE_NAME)
	@git add $(UPDATED_FILE_NAME)

check:
	@test -n '$(APPLICATION_NAME)' && test -n '$(PACKAGE_NAME)'&& test -n '$(FILE_NAME)' || { \
		echo "Usage:"; \
		echo "APPLICATION_NAME=new-application-name PACKAGE_NAME=new.pkg.name FILE_NAME=file-name make $(MAKECMDGOALS)"; \
		exit 1; \
	}

DASH:=-
UNDERSCORE:=_
DOT:=.
SLASH:=/

INITIAL_APPLICATION_NAME=gradle_quickstart
INITIAL_PACKAGE_NAME=org.grumpyf0x48
INITIAL_PACKAGE_PATH=$(subst $(DOT),$(SLASH),$(INITIAL_PACKAGE_NAME))

UPDATED_APPLICATION_NAME=$(subst $(DASH),$(UNDERSCORE),$(APPLICATION_NAME))
PACKAGE_PATH=$(subst $(DOT),$(SLASH),$(PACKAGE_NAME))
UPDATED_FILE_NAME=$(shell echo $(FILE_NAME) | sed -e "s#$(INITIAL_APPLICATION_NAME)#$(UPDATED_APPLICATION_NAME)#" -e "s#$(INITIAL_PACKAGE_PATH)#$(PACKAGE_PATH)#")
