function getCheckbox (name: string) {
  return $(`my-tube-checkbox input[id=${name}]`).parentElement()
}

async function selectCustomSelect (id: string, valueLabel: string) {
  await $(`[formcontrolname=${id}] .ng-arrow-wrapper`).click()

  const option = await $$(`[formcontrolname=${id}] .ng-option`).filter(async o => {
    const text = await o.getText()

    return text.trimStart().startsWith(valueLabel)
  }).then(options => options[0])

  await option.waitForDisplayed()

  return option.click()
}

export {
  getCheckbox,
  selectCustomSelect
}
