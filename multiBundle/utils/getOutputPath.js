const path = require('path');

/**
 * 生成bundle包输出目录
 * @param {*} platform
 * @returns
 */
function getBundleOutputPath(platform) {
  if (platform === 'android') {
    return path.resolve(__dirname, `../../android/app/src/main/assets/`);
  } else if (platform === 'ios') {
    // todo
  }
}

/**
 * 生成资源文件输出目录
 * @param {*} platform
 * @returns
 */
function getAssetsOutputPath(platform) {
  if (platform === 'android') {
    return path.resolve(__dirname, `../../android/app/src/main/res/`);
  } else if (platform === 'ios') {
    // todo
  }
}

/**
 * 生成业务包输出路径
 * @param {*} platform
 * @param {*} componentName
 * @returns
 */
function genBuzBundleOutputPath(platform, componentName) {
  if (platform === 'android') {
    return path.resolve(__dirname, `../buzBundle/${componentName}/`);
  } else if (platform === 'ios') {
    // todo
  }
}

/**
 * 生成业务资源输出路径
 * @param {*} platform
 * @param {*} componentName
 * @returns
 */
function genBuzAssetsOutputPath(platform, componentName) {
  if (platform === 'android') {
    return path.resolve(__dirname, `../buzBundle/${componentName}/assets/`);
  } else if (platform === 'ios') {
    // todo
  }
}

module.exports = {
  getBundleOutputPath,
  getAssetsOutputPath,
  genBuzBundleOutputPath,
  genBuzAssetsOutputPath,
};
