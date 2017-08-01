# Makefile -- Populate K8S entities

USER = $(shell id -un)

# $ kubectl config get-contexts
# CURRENT   NAME                         CLUSTER                      AUTHINFO                     NAMESPACE
#           minikube                     minikube                     minikube                     my-echo-test
# *         mounthood.george.hpccp.net   mounthood.george.hpccp.net   mounthood.george.hpccp.net   
#           default                      minikube                     minikube                     kowalskif-dev

K8S_CONTEXT =	$(shell kubectl config get-contexts | awk '/^\*/{print $$2}')
K8S_CLUSTER =	$(shell kubectl config get-contexts | awk '/^\*/{print $$3}')
K8S_USER =	$(shell kubectl config get-contexts | awk '/^\*/{print $$4}')
K8S_NAMESPACE =	$(shell kubectl config get-contexts | awk '/^\*/{print $$5}')

ifeq ($(K8S_CONTEXT),)
K8S_CONTEXT = $(shell git config --global k8s.context)
endif
ifeq ($(K8S_CLUSTER),)
K8S_CLUSTER = $(shell git config --global k8s.cluster)
endif
ifeq ($(K8S_USER),)
K8S_USER = $(shell git config --global k8s.user)
endif
ifeq ($(K8S_NAMESPACE),)
K8S_NAMESPACE = $(shell git config --global k8s.namespace)
endif

ifeq ($(K8S_CONTEXT),)
K8S_CONTEXT = default
endif
ifeq ($(K8S_CLUSTER),)
K8S_CLUSTER = minikube
K8S_USER = minikube
endif
ifeq ($(K8S_NAMESPACE),)
K8S_NAMESPACE = $(USER)-dev
endif

$(info *** K8S_CLUSTER: $(K8S_CLUSTER))
$(info *** K8S_USER: $(K8S_USER))
$(info *** K8S_NAMESPACE: $(K8S_NAMESPACE))

.PHONY: k8s-setup k8s-deploy
reverse = $(if $(1),$(call reverse,$(wordlist 2,$(words $(1)),$(1)))) $(firstword $(1))


K8_DIRS = build/k8s/deploy
K8_FILES := $(foreach dir,$(K8_DIRS),$(wildcard $(dir)/*.yaml))

default: help

help:
	@echo "usage: make k8s-setup | k8s-deploy"
	@echo
	@echo "	k8s-setup: setup a k8s context according to K8S_CLUSTER, K8S_USER and K8S_NAMESPACE"
	@echo "	k8s-deploy: deploy every K8S resources (K8S_FILES) files in the current context"
	@echo
	@echo "	K8_FILES: $(K8_FILES)"

k8s-setup:
ifeq ($(K8S_CLUSTER),minikube)
	minikube status | grep -n "localkube: Running"
endif
	kubectl config set-context $(K8S_CONTEXT) --cluster=$(K8S_CLUSTER) --namespace=$(K8S_NAMESPACE) --user=$(K8S_USER)
	kubectl config use-context $(K8S_CONTEXT)
	kubectl get namespace $(K8S_NAMESPACE) 1>/dev/null 2>&1 || kubectl create namespace $(K8S_NAMESPACE)

k8s-apply: $(K8_FILES)
ifeq ($(K8S_CLUSTER),minikube)
	minikube status | grep -n "localkube: Running"
endif
	$(foreach file,$(^),kubectl apply --namespace $(K8S_NAMESPACE) -f $(file) &&) true

k8s-delete: $(call reverse,$(K8_FILES))
ifeq ($(K8S_CLUSTER),minikube)
	minikube status | grep -n "localkube: Running"
endif
	$(foreach file,$(^),kubectl delete --namespace $(K8S_NAMESPACE) -f $(file) && sleep 5 &&) true
