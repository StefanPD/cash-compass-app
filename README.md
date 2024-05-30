# Cash compass app

## Kubernetes configuration

### Prerequisites

* Running kubernetes cluster. Use [Orbstack](https://orbstack.dev/) or [kind](https://kind.sigs.k8s.io/)
* Helm

### Install kube-prometheus-stack

* Add helm repository: `helm repo add prometheus-community https://prometheus-community.github.io/helm-charts` 
* Update repository: `helm repo update`
* Install kube-prometheus-stack from `kubernetes/helm/kube-prometheus-stack` directory: `helm upgrade -f values.yml kube-prometheus-stack --create-namespace --namespace kube-prometheus-stack prometheus-community/kube-prometheus-stack`