#!/usr/bin/env bash

default_max_receive="3"
queues=("transacoes-financeiras-processadas")

until awslocal sqs list-queues > /dev/null 2>&1; do
  echo "Waiting for LocalStack SQS..."
  sleep 2
done

for queue in ${queues[*]}; do
    echo "Creating $queue dependencies"
    awslocal sqs create-queue --queue-name $queue-dlq
    awslocal sqs create-queue --queue-name $queue --attributes '{"VisibilityTimeout": "120", "DelaySeconds": "1", "RedrivePolicy": "{\"deadLetterTargetArn\":\"arn:aws:sqs:sa-east-1:000000000000:'$queue'-dlq\",\"maxReceiveCount\": \"'$default_max_receive'\"}"}'
    echo ""
done